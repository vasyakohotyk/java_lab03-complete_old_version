package Services;

public class IdService {
    private static Long id = 1L;
    private IdService(){}
    public static synchronized Long createId(){
        return id++;
    }
}
