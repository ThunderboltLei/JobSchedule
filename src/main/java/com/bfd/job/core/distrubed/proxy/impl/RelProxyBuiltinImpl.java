package com.bfd.job.core.distrubed.proxy.impl;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.LinkedHashSet;
import java.util.Scanner;

import com.bfd.job.core.distrubed.proxy.RelProxyBuiltin;
import com.bfd.job.core.distrubed.proxy.listener.CommandListener;
import com.innowhere.relproxy.jproxy.JProxyScriptEngine;
import com.innowhere.relproxy.jproxy.JProxyScriptEngineFactory;

/**
 * @author: BFD474
 *
 * @description:
 */
public class RelProxyBuiltinImpl implements RelProxyBuiltin {

	protected JProxyScriptEngine jProxyEngine = null;
	// protected LinkedHashSet<OutputListener> outListeners = new
	// LinkedHashSet<OutputListener>();
	protected LinkedHashSet<CommandListener> commandListeners = new LinkedHashSet<CommandListener>();

	@Override
	public JProxyScriptEngine getJProxyScriptEngine() {
		if (jProxyEngine == null)
			jProxyEngine = (JProxyScriptEngine) JProxyScriptEngineFactory
					.create().getScriptEngine();
		return jProxyEngine;
	}

	public JProxyScriptEngine getJProxyScriptEngineIfConfigured() {
		if (jProxyEngine == null || !jProxyEngine.isEnabled())
			return null;
		return jProxyEngine;
	}

	@Override
	public void addCommandListener(CommandListener listener) {
		JProxyScriptEngine jProxy = getJProxyScriptEngineIfConfigured();
		if (jProxy != null) {
			listener = jProxy.create(listener, CommandListener.class);
		}
		commandListeners.add(listener);
	}

	@Override
	public void removeCommandListener(CommandListener listener) {
		JProxyScriptEngine jProxy = getJProxyScriptEngineIfConfigured();
		if (jProxy != null) {
			listener = jProxy.create(listener, CommandListener.class);
		}
		commandListeners.remove(listener);
	}

	@Override
	public int getCommandListenerCount() {
		return commandListeners.size();
	}

	@Override
	public void runLoop(InputStream in, PrintStream out) {
		Scanner scanner = new Scanner(in);
		while (true) {
			out.print("Enter phrase:");
			String input = scanner.nextLine();

			// out.println("Command list:");
			// for (OutputListener listener : outListeners) {
			// listener.write(out);
			// }

			out.print("Enter command (or quit):");
			String command = scanner.nextLine();

			if ("quit".equals(command)) {
				break;
			}
			for (CommandListener listener : commandListeners) {
				listener.execute(command, input, out);
			}
		}
	}

}
