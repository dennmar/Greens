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
    username = request.form['username']
    password = request.form['password']

    if current_user.is_authenticated:
        return jsonify(f'Already logged in as {current_user.username}')

    # NOTE: need error handling and check if already logged in
    matching_user = user.User.query.filter_by(username=username).first()
    if matching_user.check_password(password):
        login_user(matching_user)
        return jsonify(f'Logged in as {username}')
    else:
        return jsonify('Invalid username or password')

@bp.route('/logout', methods=['POST'])
@login_required
def logout():
    """Log the user out.

    Returns:
        A flask.Response describing the result.
    """
    prev_username = current_user.username
    logout_user()
    return jsonify(f'Logged out of {prev_username}')    
