package com.cybernostics.nanorest.integrationtest;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.catalina.Context;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class EmbeddedTomcatServer {

	private CountDownLatch cdl = new CountDownLatch(1);

	private Tomcat tomcat;

	private Class<?> springConfigClass;

	private static final int NOT_SET = -1;

	private int port = NOT_SET;

	public EmbeddedTomcatServer( Class<?> springConfigClass) {
		this(0, springConfigClass);
	}

	public EmbeddedTomcatServer(int port, Class<?> springConfigClass) {
		this.port = port;
		this.springConfigClass = springConfigClass;
	}

	public String getRootURL() {
		return String.format("http://localhost:%d", this.port);
	}

	public void startServer() {

		if (tomcat == null) {
			tomcat = new Tomcat();
			tomcat.setPort(port);

			File base = new File("");
			Context rootCtx = tomcat.addContext("/", base.getAbsolutePath());
			AnnotationConfigWebApplicationContext aactx = new AnnotationConfigWebApplicationContext();
			aactx.register(this.springConfigClass);
			DispatcherServlet dispatcher = new DispatcherServlet(aactx);
			Tomcat.addServlet(rootCtx, "SpringMVC", dispatcher);
			rootCtx.addServletMapping("/*", "SpringMVC");
			tomcat.getServer().addLifecycleListener(new LifecycleListener() {

				@Override
				public void lifecycleEvent(LifecycleEvent event) {
					if (event.getType().equals(Lifecycle.AFTER_START_EVENT)) {
						cdl.countDown();
					}
				}
			});

			try {
				tomcat.start();
				if (this.port == NOT_SET) {
					this.port = tomcat.getServer().getPort();
				}
			} catch (LifecycleException e) {
				e.printStackTrace();
			}
		}
	}

	public void stopServer() {
		if(tomcat!=null) {
			try {
				tomcat.getServer().stop();
			} catch (LifecycleException e) {
				e.printStackTrace();
			}
		}
	}

	public void awaitServerStart() {
		awaitServerStart(30, TimeUnit.SECONDS);
	}

	public void awaitServerStart(long timeout, TimeUnit unit) {
		try {
			assertThat("Server Started correctly",
					cdl.await(timeout, unit), is(true));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
