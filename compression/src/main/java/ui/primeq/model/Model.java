package ui.primeq.model;

import ui.primeq.FileManager;
import ui.primeq.config.Config;
import ui.primeq.optimizer.Adam;
import ui.primeq.optimizer.FunctionManager;

public class Model {
    private final Config config;
    private final FileManager fileManager;
    private final FunctionManager functionManager;
    private final Adam opt;

    public Model(Config config){
        this.config = config;
        this.fileManager = new FileManager();
        this.functionManager = new FunctionManager(this.config);
        this.opt = new Adam(this.config);
    }

    public FileManager getFileManager() {
        return this.fileManager;
    }

    public FunctionManager getFunctionManager() {
        return this.functionManager;
    }

    public Adam getOptimizer() {
        return this.opt;
    }

    public Config getConfig() {
        return this.config;
    }
}
