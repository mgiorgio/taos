package edu.uci.ics.grading.gradersassignment.strategies;

import edu.uci.ics.grading.gradersassignment.GradersAssigner;


public enum AssigningStrategies {
	sequential(SequentialAssigner.class), random(RandomGradersAssigner.class);

	private Class<? extends GradersAssigner> implementation;

	private AssigningStrategies(Class<? extends GradersAssigner> implementation) {
		this.implementation = implementation;
	}

	public GradersAssigner createAssigner() {
		try {
			return this.implementation.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("This should not happen.");
		}
	}

}
