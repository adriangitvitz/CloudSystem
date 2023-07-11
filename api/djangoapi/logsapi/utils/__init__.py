class Pathstrconverter:
    regex = '[A-Za-z0-9]{1,10}' # accept strings only with 10 max length

    def to_python(self, value):
        return str(value)
    
    def to_url(self, value):
        return str(value)