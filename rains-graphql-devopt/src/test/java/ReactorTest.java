import com.fasterxml.jackson.databind.ObjectMapper;
import com.rains.graphql.common.file.FileUtils;
import com.rains.graphql.common.utils.JsonMapper;
import com.rains.graphql.common.utils.StringUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReactorTest {

    @Test
    public void fluxTest(){
        final StringBuilder sb = new StringBuilder();
        Flux.just("a","b","c","d").thenMany(sb::append).subscribe(System.out::println);
    }

    @Test
    public void orgTest() throws IOException {

        InputStream in = ReactorTest.class.getResourceAsStream("org.json");
//        System.out.println(ReactorTest.class.getResource("org.json").getPath());
//        System.out.println(IOUtils.readLines(in));
        JsonMapper jsonMapper = new JsonMapper();
        ObjectMapper mapper = jsonMapper.getJacksonMapper();
        List<Map<String, Object>> listOrg= mapper.readValue(in, List.class);

        System.out.println(listOrg);

        List<Map<String, Object>> list = removeDuplicate(listOrg);

        List<Map<String, Object>> topList= findTreeTopNodeData(list);
    }

    // 去掉重复的
    private List<Map<String, Object>> removeDuplicate(List<Map<String, Object>> listOrg) {
        if (listOrg == null || listOrg.isEmpty())
            return listOrg;

        List<Map<String, Object>> list = new ArrayList<>();

        // key为ORG_REALATION_ID, value为1
        Map<String, Integer> orgRealIds = new HashMap<>();

        for (Map<String, Object> org : listOrg) {
            if(null == org.get("orgRealationId")){
                System.out.println(org);
            }
            String orgRealId = org.get("orgRealationId").toString();
            if (orgRealIds.get(orgRealId) == null) {
                list.add(org);
                orgRealIds.put(orgRealId, 1);
            }
        }

        return list;
    }

    private List<Map<String, Object>> findTreeTopNodeData(List<Map<String, Object>> list ){
        List<Map<String, Object>> result=new ArrayList<>();
        for (Map<String, Object> map : list) {
            boolean isTop=true;
            for (Map<String, Object> map2 : list) {
                if(map2.get("orgRealationId").toString().equals(map.get("parentID").toString())) {
                    isTop=false;
                    break;
                }
            }
            if(isTop) {
                result.add(map);
            }
        }
        return result;

    }
}
