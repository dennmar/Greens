import flask
from flask import Blueprint, jsonify, request
from flask_login import login_user, logout_user, login_required, current_user

from .. import db
from ..models import user

bp = Blueprint('auth', __name__, url_prefix='/auth')

@bp.route('/login', methods=['POST'])
def login():
    """Login the user.

    Returns:
        A flask.Response describing the result.
    """
    request_json = request.get_json()
    username = request_json['username']
    password = request_json['password']

    if current_user.is_authenticated:
        return flask.make_response({}, 200)

    # NOTE: need error handling
    matching_user = user.User.query.filter_by(username=username).first()
    if matching_user.check_password(password):
        login_user(matching_user)
        response_code = 200
    else:
        response_code = 401
        
    return flask.make_response({}, response_code)

@bp.route('/logout', methods=['POST'])
@login_required
def logout():
    """Log the user out.

    Returns:
        A flask.Response describing the result.
    """
    prev_username = current_user.username
    logout_user()
    return flask.make_response({}, 200)
