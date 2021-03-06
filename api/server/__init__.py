import os
import pathlib
from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from flask_login import LoginManager

db = SQLAlchemy()
login_manager = LoginManager()

def create_app(test_config=None):
    app = Flask(__name__, instance_relative_config=True)

    if test_config is None:
        app.config.from_pyfile('config.py')
    else:
        app.config.from_mapping(test_config)
    
    app.config.update(SECRET_KEY=os.urandom(24))
    db.init_app(app)
    login_manager.init_app(app)
    
    # register all routes
    from .routes import user as user_routes
    from .routes import auth as auth_routes
    app.register_blueprint(user_routes.bp)
    app.register_blueprint(auth_routes.bp)

    # reset and create the initial database
    from .models import user as user_model
    with app.app_context():
        db.drop_all()
        db.create_all()

    return app