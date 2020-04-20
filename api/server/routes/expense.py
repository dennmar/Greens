import flask
from flask import Blueprint, jsonify, request
from flask_jwt_extended import jwt_required

from .. import db
from ..models import user, expense

# NOTE: must re
bp = Blueprint('expense', __name__, url_prefix='/user/<int:user_id>/expense')

@bp.route('/', methods=['GET', 'POST'])
@jwt_required
def expense_view(user_id):
    """Find all the user's expenses or store a new expense for the user.

    Returns:
        A flask.Response containing all the user's expenses for a GET request
        or containing the id of the created expense for a POST request.
    """
    resp_body = {}
    resp_code = 400
    current_user = user.User.query.get(user_id)
    
    if request.method == 'GET':
        if current_user is None:
            resp_body = {'msg': 'Invalid user id', 'expense': None}
            resp_code = 401
        else:
            user_expenses = [exp.to_dict() for exp in current_user.expenses]
            resp_body = {'msg': None, 'expenses': user_expenses}
            resp_code = 200
    elif request.method == 'POST':
        if current_user is None:
            resp_body = {'msg': 'Invalid user id', 'created_id': None}
            resp_code = 401
        else:
            request_json = request.get_json()
            cost = request_json['cost']
            description = request_json['description']

            # NOTE: need validation and error handling
            new_expense = expense.Expense(user_id=current_user.id, cost=cost,
                description=description)
            db.session.add(new_expense)
            db.session.commit()
            db.session.close()

            resp_body = {'msg': None, 'created_id': new_expense.id}
            resp_code = 200
    
    return flask.make_response(resp_body, resp_code)

@bp.route('/<int:expense_id>', methods=['GET', 'PUT', 'DELETE'])
@jwt_required
def specific_expense_view(user_id, expense_id):
    """Return or update a specific expense (that is associated with the user).

    Returns:
        A flask.Response describing the result.
    """
    resp_body = {}
    resp_code = 400
    current_user = user.User.query.get(user_id)
    exp = None

    # NOTE: need error handling and session management
    if current_user is None:
        resp_body['msg'] = 'Invallid user id'
        resp_code = 401
    else:
        exp = expense.Expense.query.filter(expense.Expense.id == expense_id,
            expense.Expense.user_id == current_user.id).first()

        if exp is None:
            resp_body['msg'] = 'Invalid expense id'
            resp_code = 404

    if request.method == 'GET':
        if exp is None:
            resp_body['expense'] = None
        else:
            resp_body = {'msg': None, 'expense': exp.to_dict()}
            resp_code = 200
    elif request.method == 'PUT':
        if exp is None:
            resp_body['updated_id'] = None
        else:
            request_json = request.get_json()
            cost = request_json['cost']
            description = request_json['description']

            exp.cost = cost
            exp.description = description

            db.session.merge(exp)
            db.session.commit()
            db.session.close()

            resp_body = {'msg': None, 'updated_id': expense_id}
            resp_code = 200
    elif request.method == 'DELETE':
        if exp is None:
            resp_body['deleted_id'] = None
        else:
            db.session.delete(exp)
            db.session.commit()
            db.session.close()

            resp_body = {'msg': None, 'deleted_id': expense_id}
            resp_code = 200

    return flask.make_response(resp_body, resp_code)