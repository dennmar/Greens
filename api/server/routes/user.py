import flask
from flask import Blueprint, jsonify, request
from flask_jwt_extended import jwt_required

from .. import db
from ..models import user 

bp = Blueprint('user', __name__, url_prefix='/user')

@bp.route('/', methods=['GET', 'POST'])
@jwt_required
def user_view():
    """Return all users or create a new user.

    Returns:
        A flask.Response containing a list of all users for a GET request or
        containing the id of the created user for a POST request.
    """
    if request.method == 'GET':
        # NOTE: should have pagination
        users = [u.to_dict() for u in user.User.query.all()]
        return flask.make_response({'msg': None, 'users': users}, 200)
    elif request.method == 'POST':
        if not request.is_json:
            error_body = {'msg': 'Must be JSON request', 'token': None}
            return flask.make_response(error_body, 400)

        request_json = request.get_json()
        username = request_json['username']
        email = request_json['email']
        password = request_json['password']
        password_hash = user.User.genr_password_hash(password)

        # NOTE: need validation and error handling
        new_user = user.User(username=username, email=email,
            password=password_hash)
        db.session.add(new_user)
        db.session.commit()

        resp_body = {'msg': None, 'created_id': new_user.id}
        return flask.make_response(resp_body, 200)

@bp.route('/search/', methods=['POST'])
@jwt_required
def search_view():
    """Return the user that matches the given username.

    Returns:
        A flask.Response containing the matching user.
    """
    if not request.is_json:
        error_body = {'msg': 'Must be JSON request', 'token': None}
        return flask.make_response(error_body, 400)

    request_json = request.get_json()
    username = request_json.get('username', None)

    if username is None:
        error_body = {'msg': 'Missing username', 'token': None}
        return flask.make_response(error_body, 400)

    matching_user = user.User.query.filter_by(username=username).first()
    if matching_user is not None:
        resp_body = {'msg': None, 'user': matching_user.to_dict()}
        return flask.make_response(resp_body, 200)
    else:
        resp_body = {'msg': 'Invalid username', 'user': None}
        return flask.make_response(resp_body, 404)