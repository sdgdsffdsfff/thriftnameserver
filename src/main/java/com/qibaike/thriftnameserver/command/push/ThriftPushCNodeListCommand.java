package com.qibaike.thriftnameserver.command.push;

import java.util.List;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;

import com.jcabi.aspects.Loggable;
import com.qibaike.thriftnameserver.rpc.Cluster;
import com.qibaike.thriftnameserver.rpc.State;
import com.qibaike.thriftnameserver.rpc.TCNode;

public class ThriftPushCNodeListCommand extends BaseThriftPushCommand<State, TCNode> {

	public ThriftPushCNodeListCommand(TCNode tcnode, List<TCNode> list) {
		super(tcnode, list);
	}

	@Override
	protected State run() throws Exception {
		String host = tcnode.getHost();
		int port = tcnode.getPort();

		TSocket transport = new TSocket(host, port, 1000);
		TProtocol protocol = new TBinaryProtocol(transport);
		Cluster.Client client = new Cluster.Client(protocol);
		transport.open();
		try {
			client.pushClusterList(this.list);
		} finally {
			if (transport.isOpen()) {
				transport.close();
			}
		}
		return State.UP;
	}

	@Override
	protected State getFallback() {
		return this.getFallback(this.tcnode);
	}

	@Loggable(value = Loggable.WARN)
	protected State getFallback(TCNode tcnode) {
		switch (tcnode.getState()) {
		case DOWN_1:
			return State.DOWN_2;
		case DOWN_2:
			return State.DOWN;
		default:
			return State.DOWN_1;
		}
	}

	@Override
	protected void logPush(TCNode tcnode) {
		/**
		 * just log
		 */
	}
}
