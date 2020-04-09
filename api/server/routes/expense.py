import flask
from flask import Blueprint, jsonify, request
from flask_login import login_required, current_user

from .. import db
from ..models import expense

bp = Blueprint('expense', __name__, url_prefix='/expense')

@bp.route('/', methods=['GET', 'POST'])
@login_required
def expense_view():
    """Find all the user's expenses or store a new expense for the user.

    Returns:
        A flask.Response containing all the user's expenses for a GET request
        or containing the id of the created expense for a POST request.
    """
    if request.method == 'GET':
        user_expenses = [exp.to_dict() for exp in current_user.expenses]
        return flask.make_response({'expenses': user_expenses}, 200)
    elif request.method == 'POST':
        request_json = request.get_json()
        cost = request_json['cost']
        description = request_json['description']

        # NOTE: need validation and error handling
        new_expense = expense.Expense(user_id=current_user.id, cost=cost,
            description=description)
        db.session.add(new_expense)
        db.session.commit()

        return flask.make_response({'created_id': new_expense.id}, 200)

@bp.route('/<int:expense_id>', methods=['GET', 'PUT', 'DELETE'])
@login_required
def specific_expense_view(expense_id):
    """Return or update a specific expense (that is associated with the user).

    Returns:
        A flask.Response describing the result.
    """
    # NOTE: need error handling and session management
    exp = expense.Expense.query.filter(expense.Expense.id == expense_id,
       expense.Expense.user_id == current_user.id).first()

    if request.method == 'GET':
        if exp is not None:
            return flask.make_response({'expense': exp.to_dict()}, 200)
        else:
            return flask.make_response({'expense': None}, 404)
    elif request.method == 'PUT':
        if exp is not None:
            request_json = request.get_json()
            cost = request_json['cost']
            description = request_json['description']

            exp.cost = cost
            exp.description = description

            db.session.merge(exp)
            db.session.commit()

            return flask.make_response({'updated_id': expense_id}, 200)
        else:
            return flask.make_response({'updated_id': None}, 404)
    elif request.method == 'DELETE':
        if exp is not None:
            db.session.delete(exp)
            db.session.commit()
            return flask.make_response({'deleted_id': expense_id}, 200)
        else:
            return flask.make_response({'deleted_id': None}, 404)
