from .. import db

class Expense(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'), nullable=False)
    cost = db.Column(db.Float, nullable=False)
    description = db.Column(db.String(50))
    
    def to_dict(self):
        return {
            'id': self.id,
            'user_id': self.user_id,
            'cost': self.cost,
            'description': self.description
        }

    def __repr__(self):
        return f'Expense {self.id}'