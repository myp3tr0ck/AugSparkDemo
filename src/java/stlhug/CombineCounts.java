package stlhug;

import org.apache.spark.api.java.function.Function2;

public class CombineCounts implements Function2<Long, Long, Long> {
	private static final long serialVersionUID = 9039567443703875613L;

	@Override
	public Long call(Long num1, Long num2) throws Exception {
		return num1 + num2;
	}
}
