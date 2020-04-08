import flask
from flask import Blueprint, jsonify, request

from .. import db
from ..models import user 

bp = Blueprint('user', __name__, url_prefix='/user')

@bp.route('/', methods=['GET', 'POST'])
def user_view():
    """Return all users or create a new user.

    Returns:
        A flask.Response containing a list of all users for a GET request or
        containing the id of the created user for a POST request.
    """
    if request.method == 'GET':
        # NOTE: should have pagination
        users = [repr(u) for u in user.User.query.all()]
        return flask.make_response({'users': users}, 200)
    elif request.method == 'POST':
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

        return flask.make_response({'created_id': new_user.id}, 200)