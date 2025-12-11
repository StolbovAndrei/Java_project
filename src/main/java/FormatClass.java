import java.util.*;

public class FormatClass {

    public String formatResult(Map<String, List<String>> songs){
        StringBuilder result = new StringBuilder();
        result.append("Найденные треки: \n\n");
        for (Map.Entry<String, List<String>> entry : songs.entrySet()){
            for(int i = 0; i < entry.getValue().size(); ++i) {
                result.append(entry.getKey()).append(" ---- ").append(entry.getValue().get(i)).append("\n\n");
            }
        }
        return result.toString();

    }

}


