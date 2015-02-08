package com.insideaem.includestrategy.api;

public interface IncludeStrategy {

	public String getType();

	public boolean isCachingDisabled();

}
