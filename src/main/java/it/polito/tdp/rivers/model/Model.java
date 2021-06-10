package it.polito.tdp.rivers.model;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import it.polito.tdp.rivers.db.RiversDAO;

public class Model {

	private RiversDAO dao;
	private List<River> rivers;
	private PriorityQueue<Flow> queue; //gli eventi sono i flow
	
	public Model() {
		
		dao= new RiversDAO();
		//rivers= new LinkedList<River>();
		rivers= dao.getAllRivers();
		
		for(River r: rivers) {
			r.setFlows(dao.getFlows(r));
		}
	}
	
	public List<River> getRivers(){
		return rivers;
	}
	public LocalDate getStartDate(River r) {
		if(!r.getFlows().isEmpty())
		return r.getFlows().get(0).getDay();
		return null;
	}
	public LocalDate getEndDate(River r) {
		if(!r.getFlows().isEmpty())
		return r.getFlows().get(r.getFlows().size()-1).getDay();
		return null;
	}
	public int numMisurazioni(River r) {
		return r.getFlows().size();
	}
	public Double avgFlusso(River r) {
		double avg=0;
		for(Flow f: r.getFlows()) {
			avg+= f.getFlow();
		}
		avg /= r.getFlows().size();
		r.setFlowAvg(avg);
		if(avg!=0)
		return avg;
		return null;
	}
	
	public SimulationResult simula(River r, double k) {
		//inizializzo la coda
		this.queue= new PriorityQueue<Flow>();
		this.queue.addAll(r.getFlows());
		
		List<Double> listC= new LinkedList<>();
		double Q=k*30* convertM3SecToM3Day(r.getFlowAvg());
		double C=Q/2;
		double fOutMin= convertM3SecToM3Day(0.8*r.getFlowAvg());
		int numDays=0;
		
		System.out.println("Q: "+Q);
		Flow flow;
		while((flow=this.queue.poll())!= null) {
			System.out.println("date: "+ flow.getDay());
			
			double fOut= fOutMin;
			
			if(Math.random()>0.95) {
				fOut= 10* fOutMin;
				System.out.println("10*fOutMin");
			}
			System.out.println("fOut: "+ fOut);
			System.out.println("fIn: "+ convertM3SecToM3Day(flow.getFlow()));
			
			//aggiungo fIn in C
			C+= convertM3SecToM3Day(flow.getFlow());
			
			if(C>Q)
				C=Q;
			if(C< fOut) {
				//non riesco a garantire la quantità minima 
				numDays++;
				C=0;
				
			}
			else {
				C -= fOut;
			}
			System.out.println("C: "+ C);
			
			listC.add(C);
				
		}
		//Calcolo la media delle capacità 
		double Cavg=0;
		for(Double c: listC) {
			Cavg+=c;
		}
		Cavg/= listC.size();
		return new SimulationResult(Cavg,numDays);
	}
	
	private double convertM3SecToM3Day(double input) {
		return input*60*60*24;
	}
}
