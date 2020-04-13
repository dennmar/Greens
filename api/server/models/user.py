from werkzeug.security import generate_password_hash, check_password_hash

from .. import db

class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(80), unique=True, nullable=False)
    email = db.Column(db.String(120), unique=True, nullable=False)
    password = db.Column(db.String(100), nullable=False)
    expenses = db.relationship('Expense', backref='user')

    @staticmethod
    def genr_password_hash(password):
        return generate_password_hash(password)

    def check_password(self, password):
        return check_password_hash(self.password, password)

    def to_dict(self):
        return {'id': self.id, 'username': self.username, 'email': self.email}

    def __repr__(self):
        return f'<User {self.username}>'