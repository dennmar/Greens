import flask
import datetime
from flask import Blueprint, jsonify, request
from werkzeug.security import check_password_hash
from flask_jwt_extended import create_access_token, jwt_required

from .. import db
from ..models import user

bp = Blueprint('auth', __name__, url_prefix='/auth')

@bp.route('/token', methods=['POST'])
def token():
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
    
    auth_password = flask.current_app.config['AUTH_PASSWORD']
    if username == flask.current_app.config['AUTH_USERNAME'] and \
            check_password_hash(auth_password, password):
        expiration = datetime.timedelta(minutes=30)
        access_token = create_access_token(identity=username,
                expires_delta=expiration)
        return flask.make_response({'msg': None, 'token': access_token}, 200)
    else:
        error_body = {'msg': 'Invalid username or password', 'token': None}
        return flask.make_response(error_body, 401)

@bp.route('/login', methods=['POST'])
@jwt_required
def login():
    """Check if the given username and password are for a valid user.

    Returns:
        A flask.Response describing the result.
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
    if matching_user is not None and matching_user.check_password(password):
        return flask.make_response({'msg': None}, 200)
    else:
        return flask.make_response({'msg': 'Invalid username or password'}, 401)