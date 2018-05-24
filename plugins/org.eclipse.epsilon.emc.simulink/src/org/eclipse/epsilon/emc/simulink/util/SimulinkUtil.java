package org.eclipse.epsilon.emc.simulink.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.epsilon.emc.simulink.engine.MatlabEngine;
import org.eclipse.epsilon.emc.simulink.engine.MatlabException;
import org.eclipse.epsilon.emc.simulink.model.SimulinkModel;
import org.eclipse.epsilon.emc.simulink.model.element.ISimulinkBlockModelElement;
import org.eclipse.epsilon.emc.simulink.model.element.ISimulinkModelElement;
import org.eclipse.epsilon.emc.simulink.model.element.SimulinkBlock;
import org.eclipse.epsilon.emc.simulink.model.element.SimulinkLine;
import org.eclipse.epsilon.emc.simulink.model.element.SimulinkPort;

public class SimulinkUtil {

	private static final String GET_BLOCK_HANDLE = "getSimulinkBlockHandle('?');";
	private static final String FIND_BLOCKS = "find_system('?', 'BlockType', '?');";
	private static final String FIND_SYSTEM = "find_system('?', 'FindAll', 'on', 'Type', 'Block');";
	private static final String FIND_DEPTH = "find_system('?', 'SearchDepth', ?, 'Type', 'Block');";
	private static final String CHILDREN = "setdiff(find_system('?', 'SearchDepth', 0, 'Type', 'Block'), "
			+ "find_system('?', 'SearchDepth', 1, 'Type', 'Block'));";

	public static String getSimpleTypeName(String type) { // OK
		if (type.indexOf("/") > -1) {
			String[] parts = type.split("/");
			return parts[parts.length - 1];
		}
		if (type.indexOf(".") > -1) {
			String[] parts = type.split("\\.");
			return parts[parts.length - 1];
		}
		return type;
	}
	
	public static String getTypePathInModel(SimulinkModel model, String type) { 
		return model.getSimulinkModelName() + "/" + getSimpleTypeName(type);
	}
	
	public static String handleMethod(ISimulinkBlockModelElement obj, String methodName, Object[] parameters) {
		Double handle = obj.getHandle();
		return handleMethod(handle, methodName, parameters);
	}
	
	public static String handleMethod(SimulinkModel obj, String methodName, Object[] parameters) {
		Double handle = obj.getHandle();
		return handleMethod(handle, methodName, parameters);
	}
	
	private static String handleMethod(Object objHandle, String methodName, Object[] parameters) {
		List<Object> list = new ArrayList<Object>();
		list.add("handle");
		list.addAll(Arrays.asList(parameters));
		return "handle = " + objHandle + "; " + handleMethod(methodName, list.toArray());
	}
	
	public static String handleMethodWithResult(ISimulinkBlockModelElement obj, String methodName, Object[] parameters) {
		Double handle = obj.getHandle();
		return handleMethodWithResult(handle, methodName, parameters);
	}
	
	public static String handleMethodWithResult(SimulinkModel obj, String methodName, Object[] parameters) {
		Double handle = obj.getHandle();
		return handleMethodWithResult(handle, methodName, parameters);
	}
	
	private static String handleMethodWithResult(Object objHandle, String methodName, Object[] parameters) {
		List<Object> list = new ArrayList<Object>();
		list.add("handle");
		list.addAll(Arrays.asList(parameters));
		return "handle = " + objHandle + "; " + handleMethodWithResult(methodName, list.toArray());
	}

	public static String handleMethodWithResult(String methodName, Object[] parameters) {
		return "result = " + handleMethod(methodName, parameters); 
	}
	
	public static String handleMethod(String methodName, Object[] parameters) {
		String cmd = methodName;
		if (parameters != null && parameters.length > 0) {
			cmd += "(" + parameters[0];
			for (int i = 1; i < parameters.length; i++) {
				cmd += ", " + "'" + String.valueOf(parameters[i]) + "'";
			}
			cmd += ");";
		}
		return cmd;
	}

	public static Double getHandle(String path, MatlabEngine engine) {
		try {
			return (Double) engine.evalWithResult(GET_BLOCK_HANDLE, path);
		} catch (MatlabException e) {
			e.printStackTrace();
		}
		return -1.0;
	}

	/** GENERAL COLLECTION */

	public static <T extends ISimulinkModelElement> List<T> getTypeList(Class<T> returnType, SimulinkModel model, MatlabEngine engine,
			Object handles) {
		return getTypeList(returnType, model, engine, handles, null);
	}
	
	public static <T extends ISimulinkModelElement> List<T> getTypeList(Class<T> returnType, SimulinkModel model, MatlabEngine engine, Object handles,
			String type) {

		List<T> list = new ArrayList<T>();

		if (handles instanceof List) {
			try{
				for (String path : (List<String>) handles) {
					T instantiate = instantiate(returnType, model, engine, null, path);
					if (instantiate!= null) list.add(instantiate);
				}
			} catch (Exception e) {
				try{
					for (Double handle : (List<Double>) handles) {
						T instantiate = instantiate(returnType, model, engine, handle, null);
						if (instantiate!= null) list.add(instantiate);
					}
				} catch (Exception ex) {}
			}
		} else if (handles instanceof String) {
			T instantiate = instantiate(returnType, model, engine, null, (String) handles);
			if (instantiate!= null) list.add(instantiate);
		} else if (handles instanceof Double) {
			T instantiate = instantiate(returnType, model, engine, (Double) handles, null);
			if (instantiate!= null) list.add(instantiate);
		}
		return list;
	}
	
	private static <T> T instantiate(Class<T> clazz, SimulinkModel model, MatlabEngine engine, Double handle,
			String path) {
		try {
			if (handle != null) {
				return clazz.getConstructor(SimulinkModel.class, MatlabEngine.class, Double.class).newInstance(model,
						engine, handle);
			} else if (path != null) {
				return clazz.getConstructor(String.class, SimulinkModel.class, MatlabEngine.class)
						.newInstance(path, model, engine);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/** SPECIFIC COLLECTION */
	
	public static List<SimulinkBlock> getSimulinkBlocks(SimulinkModel model,
			MatlabEngine engine, Object handles) {
		return SimulinkUtil.getTypeList(SimulinkBlock.class, model, engine, handles);
	}
	
	public static List<SimulinkLine> getSimulinkLines(SimulinkModel model,
			MatlabEngine engine, Object handles) {
		return SimulinkUtil.getTypeList(SimulinkLine.class, model, engine, handles);
	}
	
	public static List<SimulinkPort> getSimulinkPorts(SimulinkModel model,
			MatlabEngine engine, Object handles) {
		return SimulinkUtil.getTypeList(SimulinkPort.class, model, engine, handles);
	}
	
	
	public static List<SimulinkBlock> getChildren(SimulinkModel model,
			MatlabEngine engine, SimulinkBlock block) {
		try {
			return getSimulinkBlocks(model, engine, engine.evalWithResult(CHILDREN, block.getPath(), block.getPath()));
		} catch (MatlabException e) {
			return Collections.emptyList();
		} 
	}
	
	public static List<SimulinkBlock> getChildren(SimulinkModel model,
			MatlabEngine engine) {
		try {
			return getSimulinkBlocks(model, engine, engine.evalWithResult(CHILDREN, model.getSimulinkModelName(), model.getSimulinkModelName()));
		} catch (MatlabException e) {
			return Collections.emptyList();
		} 
	}
	
	public static List<ISimulinkModelElement> findBlocks(SimulinkModel model,
			MatlabEngine engine, Integer depth) {
		try {
			return getSimulinkBlocks(model, engine, engine.evalWithResult(FIND_DEPTH, model.getSimulinkModelName(), depth))
					.stream().map(e -> (ISimulinkBlockModelElement) e).collect(Collectors.toList());
		} catch (MatlabException e) {
			return Collections.emptyList();
		} 
	}
	
	public static List<ISimulinkModelElement> getAllSimulinkBlocksFromModel(SimulinkModel model,
			MatlabEngine engine) {
		try {
			return getSimulinkBlocks(model, engine, engine.evalWithResult(FIND_SYSTEM, model.getSimulinkModelName()))
					.stream().map(e -> (ISimulinkBlockModelElement) e).collect(Collectors.toList());
		} catch (MatlabException e) {
			return Collections.emptyList();
		} 
	}
	
	public static List<ISimulinkModelElement> getAllSimulinkBlocksFromModel(SimulinkModel model,
			MatlabEngine engine, String type) {
		try {
			final String simpleType = type;
			Object blocks = engine.evalWithResult(FIND_BLOCKS, model.getSimulinkModelName(), simpleType);
			return getSimulinkBlocks(model, engine, blocks)
					.stream().map(e -> (ISimulinkBlockModelElement) e).collect(Collectors.toList());
		} catch (MatlabException e) {
			return Collections.emptyList();
		} 
	}

}