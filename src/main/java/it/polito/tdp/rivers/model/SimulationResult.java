package it.polito.tdp.rivers.model;

public class SimulationResult {

	private double avgC;
	private int numDays;
	public SimulationResult(double avgC, int numDays) {
		super();
		this.avgC = avgC;
		this.numDays = numDays;
	}
	public double getAvgC() {
		return avgC;
	}
	public void setAvgC(double avgC) {
		this.avgC = avgC;
	}
	public int getNumDays() {
		return numDays;
	}
	public void setNumDays(int numDays) {
		this.numDays = numDays;
	}
	
	
}
