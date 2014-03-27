package com.scejtesting.core.concordion.command;

/**
 * User: Fedorovaleks
 * Date: 20.03.14
 */
public interface ScejCommand {

    public CommandType getCommandType();

    public enum CommandType {
        SetGlobalVariable("setGlobal"),
        RegisterGlobalVariables("registerGlobals");

        private String specificationCommand;

        private CommandType(String specificationCommand) {
            this.specificationCommand = specificationCommand;
        }

        public String getSpecificationCommand() {
            return specificationCommand;
        }
    }

}
