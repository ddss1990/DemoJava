/**
 * 
 */
package com.dss.java.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DSS
 *
 */
public class Cat extends ArrayList<String> {
	private String name;
	private int legs;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the legs
	 */
	public int getLegs() {
		return legs;
	}
	/**
	 * @param legs the legs to set
	 */
	public void setLegs(int legs) {
		this.legs = legs;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Cat [name=" + name + ", legs=" + legs + "]";
	}
	
	
}
