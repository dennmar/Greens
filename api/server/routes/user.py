import flask
from flask import Blueprint, jsonify, request

from .. import db
from ..models import user 

bp = Blueprint('user', __name__, url_prefix='/user')

@bp.route('/', methods=['GET'])
def user_view():
    """Return all users stored in database.

    Returns:
        A flask.Response containing a list of all users.
    """
    # NOTE: should have pagination
    return jsonify(users=[repr(u) for u in user.User.query.all()])

@bp.route('/new', methods=['POST'])
def user_new_view():
    """Store a new user in the database.

    Returns:
        A flask.Response containing the ID of the new user.
    """
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

    return jsonify(new_id=new_user.id)