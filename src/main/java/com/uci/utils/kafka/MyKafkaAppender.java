package com.uci.utils.kafka;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.core.AbstractLifeCycle;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.AbstractManager;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.mom.kafka.KafkaAppender;
import org.apache.logging.log4j.core.appender.mom.kafka.KafkaManager;
import org.apache.logging.log4j.core.appender.mom.kafka.KafkaAppender.Builder;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.layout.SerializedLayout;

import com.uci.utils.telemetry.LogTelemetryBuilder;
import com.uci.utils.telemetry.TelemetryLogger;

@Plugin(name = "myKafka", category = "Core", elementType = "appender", printObject = true)
public final class MyKafkaAppender extends AbstractAppender {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
     * Builds KafkaAppender instances.
     * @param <B> The type to build
     */
    public static class Builder<B extends Builder<B>> extends AbstractAppender.Builder<B>
            implements org.apache.logging.log4j.core.util.Builder<MyKafkaAppender> {

        @PluginAttribute("topic")
        private String topic;

        @PluginAttribute("key")
        private String key;

        @PluginAttribute(value = "syncSend", defaultBoolean = true)
        private boolean syncSend;

        @SuppressWarnings("resource")
        @Override
        public MyKafkaAppender build() {
            final Layout<? extends Serializable> layout = getLayout();
            if (layout == null) {
                AbstractLifeCycle.LOGGER.error("No layout provided for KafkaAppender");
                return null;
            }
            final KafkaManager kafkaManager = KafkaManager.getManager(getConfiguration().getLoggerContext(),
                getName(), topic, syncSend, getPropertyArray(), key);
            return new MyKafkaAppender(getName(), layout, getFilter(), isIgnoreExceptions(), kafkaManager,
                    getPropertyArray());
        }

        public String getTopic() {
            return topic;
        }

        public boolean isSyncSend() {
            return syncSend;
        }

        public B setTopic(final String topic) {
            this.topic = topic;
            return asBuilder();
        }

        public B setSyncSend(final boolean syncSend) {
            this.syncSend = syncSend;
            return asBuilder();
        }

    }

	private final KafkaManager manager;
    
    private MyKafkaAppender(final String name, final Layout<? extends Serializable> layout, final Filter filter,
            final boolean ignoreExceptions, final KafkaManager manager, final Property[] properties) {
        super(name, filter, layout, ignoreExceptions, properties);
        this.manager = Objects.requireNonNull(manager, "manager");
    }
    
    /**
     * Creates a builder for a KafkaAppender.
     * @return a builder for a KafkaAppender.
     */
    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return new Builder<B>().asBuilder();
    }

    
    public static MyKafkaAppender createAppender(
            final Layout<? extends Serializable> layout,
            final Filter filter,
            final String name,
            final boolean ignoreExceptions,
            final String topic,
            final Property[] properties,
            final Configuration configuration,
            final String key) {

        if (layout == null) {
            AbstractLifeCycle.LOGGER.error("No layout provided for KafkaAppender");
            return null;
        }
        final KafkaManager kafkaManager = KafkaManager.getManager(configuration.getLoggerContext(), name, topic,
            true, properties, key);
        return new MyKafkaAppender(name, layout, filter, ignoreExceptions, kafkaManager, null);
    }

	@Override
	public void append(final LogEvent event) {
		String className = TelemetryLogger.class.toString().replace("class ", "");
//    	System.out.println("Custom: "+event.getLoggerName()+", class: "+LogTelemetryBuilder.class.toString().replace("class ", ""));
//    	System.out.println(event.getLoggerName().equals(LogTelemetryBuilder.class.toString().replace("class ", "")));
//    	LOGGER.info("Custom: "+event.getLoggerName());
		if (event.getLoggerName().startsWith("org.apache.kafka")) {
			LOGGER.warn("Recursive logging from [{}] for appender [{}].", event.getLoggerName(), getName());
		} else if(event.getLoggerName().equals(className)) {
			try {
				if (getLayout() != null) {
					manager.send(getLayout().toByteArray(event));
				} else {
					manager.send(event.getMessage().getFormattedMessage().getBytes(StandardCharsets.UTF_8));
				}
			} catch (final Exception e) {
				LOGGER.error("Unable to write to Kafka [{}] for appender [{}].", manager.getName(), getName(), e);
				throw new AppenderLoggingException("Unable to write to Kafka in appender: " + e.getMessage(), e);
			}
		}
	}

	@Override
	public void start() {
		super.start();
		manager.startup();
	}

	@Override
	public void stop() {
		super.stop();
		manager.release();
	}
}