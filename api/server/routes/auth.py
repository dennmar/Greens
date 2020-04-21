import flask
import datetime
from flask import Blueprint, jsonify, request
from werkzeug.security import check_password_hash
from flask_jwt_extended import (
        create_access_token, create_refresh_token, jwt_required,
        jwt_refresh_token_required, get_jwt_identity
)

from .. import db
from ..models import user

bp = Blueprint('auth', __name__, url_prefix='/auth')

@bp.route('/login', methods=['POST'])
def login():
    """Create an access and refresh token for a user.

    Returns:
        A flask.Response describing the result.
    """
    error_body = {'access_token': None, 'refresh_token': None,
            'username': None, 'user_id': None}

    if not request.is_json:
        error_body['msg'] = 'Must be JSON request'
        return flask.make_response(error_body, 400)

    request_json = request.get_json()
    username = request_json.get('username', None)
    password = request_json.get('password', None)
    
    if username is None:
        error_body['msg'] = 'Missing username'
        return flask.make_response(error_body, 400)
    elif password is None:
        error_body['msg'] = 'Missing password'
        return flask.make_response(error_body, 400)

    matching_user = user.User.query.filter_by(username=username).first()
    if matching_user is not None and matching_user.check_password(password):
        access_token = create_access_token(identity=username, fresh=True)
        refresh_token = create_refresh_token(identity=username)
        resp_body = {'msg': None, 'access_token': access_token,
                'refresh_token': refresh_token, 'username': username,
                'user_id': matching_user.id}
        return flask.make_response(resp_body, 200)
    else:
        error_body['msg'] = 'Invalid username or password'
        return flask.make_response(error_body, 401)

@bp.route('/refresh', methods=['POST'])
@jwt_refresh_token_required
def refresh():
    """Create a non-fresh access token from the given refresh token.
    
    Returns:
        A flask.Response describing the result.
    """
    username = get_jwt_identity()
    access_token = create_access_token(identity=username, fresh=False)
    resp_body = {'msg': None, 'access_token': access_token}
    return flask.make_response(resp_body, 200)

@bp.route('/refresh-login', methods=['POST'])
def refresh_login():
    """Create a fresh access token for the user.

    Returns:
        A flask.Response describing the result.
    """
    error_body = {'access_token': None}

    if not request.is_json:
        error_body['msg'] = 'Must be JSON request'
        return flask.make_response(error_body, 400)

    request_json = request.get_json()
    username = request_json.get('username', None)
    password = request_json.get('password', None)
    
    if username is None:
        error_body['msg'] = 'Missing username'
        return flask.make_response(error_body, 400)
    elif password is None:
        error_body['msg'] = 'Missing password'
        return flask.make_response(error_body, 400)

    matching_user = user.User.query.filter_by(username=username).first()
    if matching_user is not None and matching_user.check_password(password):
        access_token = create_access_token(identity=username, fresh=True)
        resp_body = {'msg': None, 'access_token': access_token}
        return flask.make_response(resp_body, 200)
    else:
        error_body['msg'] = 'Invalid username or password'
        return flask.make_response(error_body, 401)