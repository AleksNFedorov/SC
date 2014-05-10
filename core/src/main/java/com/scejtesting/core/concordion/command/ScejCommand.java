package com.scejtesting.core.concordion.command;

import org.concordion.api.Command;

/**
 * User: Fedorovaleks
 * Date: 20.03.14
 */
public interface ScejCommand extends Command {

    String SCEJ_TESTING_NAME_SPACE = "http://www.scejtesting.com/2014";

    public String getCommandName();
}
