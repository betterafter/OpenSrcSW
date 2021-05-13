
class Calc:
    def sum(self, a, b):
        result = a + b
        print("{0} + {1} = {2} 입니다.".format(a,b,result))
    def sub(self, a, b):
        result = a - b
        print("{0} - {1} = {2} 입니다.".format(a,b,result))
    def mul(self, a, b):
        result = a * b
        print("{0} * {1} = {2} 입니다.".format(a,b,result))
    def div(self, a, b):
        if(b == 0):
            print("나눌 수 없습니다.")
        else:
            result = a / b
            print("{0} / {1} = {2} 입니다.".format(a,b,result))



