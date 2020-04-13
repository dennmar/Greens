import flask
from flask import Blueprint, jsonify, request
from werkzeug.security import check_password_hash
from flask_jwt_extended import create_access_token

from .. import db
from ..models import user

bp = Blueprint('auth', __name__, url_prefix='/auth')

@bp.route('/token', methods=['POST'])
def login():
    """Return an access token.

    Returns:
        A flask.Response containing an error message and token.
    """
    if not request.is_json:
        error_body = {'msg': 'Must be JSON request', 'token': None}
        return flask.make_response(error_body, 400)
        
    request_json = request.get_json()
    username = request_json.get('username', None)
    password = request_json.get('password', None)

    if username is None:
        error_body = {'msg': 'Missing username', 'token': None}
        return flask.make_response(error_body, 400)
    elif password is None:
        error_body = {'msg': 'Missing password', 'token': None}
        return flask.make_response(error_body, 400)
    
    matching_user = user.User.query.filter_by(username=username).first()
    auth_password = flask.current_app.config['AUTH_PASSWORD']
    if username == flask.current_app.config['AUTH_USERNAME'] and \
            check_password_hash(auth_password, password):
        # NOTE: should set expiration for token
        access_token = create_access_token(identity=username)
        return flask.make_response({'msg': None, 'token': access_token}, 200)
    else:
        error_body = {'msg': 'Invalid username or password', 'token': None}
        return flask.make_response(error_body, 401)