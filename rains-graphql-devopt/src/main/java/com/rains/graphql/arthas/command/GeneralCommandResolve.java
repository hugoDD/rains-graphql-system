package com.rains.graphql.arthas.command;

import com.rains.graphql.arthas.entity.commands.General;
import com.rains.graphql.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
public class GeneralCommandResolve extends BaseCmdResolve {

    @Override
    public Function<List<String>, List<General>> transform() {
        return rs -> {
            List<General> list = new ArrayList<>();
            rs.remove(PROMPT );
            Iterator<String> it = rs.iterator();
            //command
            it.next();
            //first
            Map<String, Integer> poit = chartPoit(it.next());
            int keyIndex = poit.get("KEY");
            int valueIndex = poit.get("VALUE");
            //分割线
            it.next();
            General nextValue = null;

            while (it.hasNext()) {

                String str = it.next();
                try {
                    String key = str.substring(keyIndex, valueIndex - 1).trim();
                    String value = str.substring(valueIndex).trim();
                    if (StringUtils.isEmpty(key)) {
                        nextValue.setValue(nextValue.getValue() + value);
                    } else {
                        General general = new General();
                        general.setKey(key);
                        general.setValue(value);
                        nextValue = general;
                        list.add(general);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(),e);
                }
            }

            return list;
        };
    }
}
