
//---------------------------------對帳單清單---------------------------

import java.util.ArrayList;
import java.util.List;

public class StatementList {
    private List<Statement> myStatementList;

    public StatementList(){
        myStatementList = new ArrayList<Statement>();
    }
    public void addStatement(Statement x){
        myStatementList.add(x);
    }
    public void showStatement(){
        for(int i = 0; i < myStatementList.size(); i++){
            System.out.println(myStatementList.get(i));
        }
    }
}
