package org.finra.datagenerator.engine.scxml.tags;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AbstractSetAssignExtension
{
	public List<Map<String, String>> performCartesianProduct(String variable, List<Map<String, String>> possibleStateList, List<String> domain)
	{
        List<Map<String, String>> productTemp = new LinkedList<>();
        for (Map<String, String> p : possibleStateList) {
            for (String value : domain) {
                HashMap<String, String> n = new HashMap<>(p);
                n.put(variable, value);
                productTemp.add(n);
            }
        }
        return productTemp;
	}
}
