import flask
from flask import Blueprint, jsonify, request

from .. import db
from ..models import user 

bp = Blueprint('user', __name__, url_prefix='/user')

@bp.route('/', methods=['GET'])
def user_view():
    """Return all users stored in database.

    Returns:

    """
    # NOTE: should have pagination
    return jsonify([repr(u) for u in user.User.query.all()])

@bp.route('/new', methods=['POST'])
def user_new_view():
    """Store a new user in the database.

    Returns:
        A flask.Response containing the username of the new user.
    """
    username = request.form['username']
    email = request.form['email']
    password = request.form['password']
    
    # NOTE: need validation and error handling
    new_user = user.User(username=username, email=email, password=password)
    db.session.add(new_user)
    db.session.commit()

    return jsonify(username)