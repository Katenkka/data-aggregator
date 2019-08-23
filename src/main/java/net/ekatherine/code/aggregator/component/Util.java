package net.ekatherine.code.aggregator.component;

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Component
public class Util {
	public <T> void consumeSuppliedIfTrue(final Consumer<T> setter, final Supplier<T> supplier, final Predicate<T> predicate) {
		final T value = supplier.get();

		if (predicate.test(value)) {
			setter.accept(value);
		}
	}

	public String sanitize(final String toSanitize) {
		return StringEscapeUtils.unescapeJava(StringUtils.trimWhitespace(Jsoup.parse(StringEscapeUtils.escapeJava(toSanitize)).text()));
	}
}
