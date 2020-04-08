import flask
from flask import Blueprint, jsonify, request
from flask_login import login_required, current_user

from .. import db
from ..models import expense

bp = Blueprint('expense', __name__, url_prefix='/expense')

@bp.route('/', methods=['GET'])
@login_required
def expense_view():
    """Find all expenses associated with the user.

    Returns:
        A flask.Response containing all the user's expenses.
    """
    user_expenses = [exp.to_dict() for exp in current_user.expenses]
    return flask.make_response({'expenses': user_expenses}, 200)

@bp.route('/new', methods=['POST'])
@login_required
def new_expense_view():
    """Store a new expense for the user.

    Returns:
        A flask.Response describing the result.
    """
    request_json = request.get_json()
    cost = request_json['cost']
    description = request_json['description']

    # NOTE: need validation and error handling
    new_expense = expense.Expense(user_id=current_user.id, cost=cost,
        description=description)
    db.session.add(new_expense)
    db.session.commit()

    return flask.make_response({'created_id': new_expense.id}, 200)