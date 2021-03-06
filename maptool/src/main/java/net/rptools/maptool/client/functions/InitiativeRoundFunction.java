/*
 *  This software copyright by various authors including the RPTools.net
 *  development team, and licensed under the LGPL Version 3 or, at your
 *  option, any later version.
 *
 *  Portions of this software were originally covered under the Apache
 *  Software License, Version 1.1 or Version 2.0.
 *
 *  See the file LICENSE elsewhere in this distribution for license details.
 */

package net.rptools.maptool.client.functions;

import java.math.BigDecimal;
import java.util.List;

import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.language.I18N;
import net.rptools.maptool.model.InitiativeList;
import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;
import net.rptools.parser.function.AbstractFunction;

/**
 * Accessor for the current initiative round
 * 
 * @author Jay
 */
public class InitiativeRoundFunction extends AbstractFunction {

    /** Handle adding one, all, all PCs or all NPC tokens. */
	private InitiativeRoundFunction() {
		super(0, 1, "getInitiativeRound", "setInitiativeRound");
	}
	
    /** singleton instance of this function */
    private final static InitiativeRoundFunction instance = new InitiativeRoundFunction();

    /** @return singleton instance */
	public static InitiativeRoundFunction getInstance() { return instance; }	
	
	/**
	 * @see net.rptools.parser.function.AbstractFunction#childEvaluate(net.rptools.parser.Parser, java.lang.String, java.util.List)
	 */
	@Override
	public Object childEvaluate(Parser parser, String functionName, List<Object> args) throws ParserException {
	    if (functionName.equals("getInitiativeRound")) {
	        return getInitiativeRound();
	    } else {
	        if (args.size() != 1)
	            throw new ParserException(I18N.getText("macro.function.setinitiativeRound.oneParam"));
	        if (MapTool.getParser().isMacroTrusted() || MapTool.getFrame().getInitiativePanel().hasGMPermission()) {
	            setInitiativeRound(args.get(0));
	            return args.get(0);
	        } else {
	            throw new ParserException(I18N.getText("macro.function.initiative.mustBeGM", functionName));
	        } // endif
        } // endif
	}
	
	/**
	 * Get the initiative round;
	 * 
	 * @return The initiative round
	 */
	public Object getInitiativeRound() {
        InitiativeList list = MapTool.getFrame().getCurrentZoneRenderer().getZone().getInitiativeList();
        return new BigDecimal(list.getRound());
	}
	
	/**
	 * Set the initiative round.
	 * 
	 * @param value New value for the round.
	 */
	public void setInitiativeRound(Object value) {
        InitiativeList list = MapTool.getFrame().getCurrentZoneRenderer().getZone().getInitiativeList();
        list.setRound(getInt(value));
	}
	
	/**
	 * Try to convert an object into an int value.
	 * 
	 * @param value Convert this value
	 * @return The integer value or 0 if no value could be determined.
	 */
	public static final int getInt(Object value) {
	    if (value == null) return 0;
	    if (value instanceof Number) {
	        return ((Number)value).intValue();
	    } else {
	        try {
	            return Integer.parseInt(value.toString());
	        } catch (NumberFormatException e) {
	            return 0;
            } // endtry
	    } // endif
	}
}
