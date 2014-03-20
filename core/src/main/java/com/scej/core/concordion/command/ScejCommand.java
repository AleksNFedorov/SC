package com.scej.core.concordion.command;

/**
 * User: Fedorovaleks
 * Date: 20.03.14
 */
public interface ScejCommand {

    public enum CommandType {
        SetGlobalVariable("setGlobal"),
        InitGlobalVariables("initGlobals");

        private String specificationCommand;

        public String getSpecificationCommand() {
            return specificationCommand;
        }

        private CommandType(String specificationCommand) {
            this.specificationCommand = specificationCommand;
        }
    }

    public CommandType getCommandType();

}
