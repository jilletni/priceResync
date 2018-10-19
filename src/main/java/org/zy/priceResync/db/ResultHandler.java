package org.zy.priceResync.db;

import java.util.List;
import java.util.Map;

public interface ResultHandler {
	public void handle(List<Map<String,Object>> rs);
}
