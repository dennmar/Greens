import os
import pathlib
from flask import Flask
from flask_sqlalchemy import SQLAlchemy
   
db = SQLAlchemy()

def create_app(test_config=None):
    app = Flask(__name__, instance_relative_config=True)

    if test_config is None:
        app.config.from_pyfile('config.py')
    else:
        app.config.from_mapping(test_config)

    db.init_app(app)
    
    from .routes import user as user_routes
    app.register_blueprint(user_routes.bp)

    from .models import user as user_model
    # reset and create the initial database
    with app.app_context():
        db.drop_all()
        db.create_all()

    return app