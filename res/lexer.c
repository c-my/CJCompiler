int a = 1;
int b =0;
int c;
char eq='=';
char ent = '\n';
char mul = 'x';
char blank = '_';

while(a<=9){
    b=1;
    while(b<a){
        print b;
        print mul;
        print a;

        c = a * b;
        print eq;
        print c;
        print blank;
        b=b+1;
    }
    println ent;
    a=a+1;
}