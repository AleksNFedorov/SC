package com.scejtesting.core.context;

import org.concordion.api.Result;
import org.concordion.api.RunnerResult;
import org.concordion.internal.util.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by aleks on 4/26/14.
 */
public class SpecificationResultRegistry {

    protected static final Logger LOG = LoggerFactory.getLogger(SpecificationResultRegistry.class);

    private final Map<Result, Collection<RunnerResult>> resultsMap = new EnumMap<Result, Collection<RunnerResult>>(Result.class);

    public SpecificationResultRegistry() {
        init();
    }

    private void init() {
        for (Result result : Result.values()) {
            resultsMap.put(result, new TreeSet<RunnerResult>(new Comparator<RunnerResult>() {
                @Override
                public int compare(RunnerResult runnerResult, RunnerResult runnerResult2) {
                    return runnerResult.hashCode() - runnerResult2.hashCode();
                }
            }));
        }

        LOG.info("Runner result instance successfully initialized");
    }

    public void addAll(SpecificationResultRegistry anotherSpecRegistry) {
        Check.notNull(anotherSpecRegistry, "Results container registry can't be null");

        for (Collection<RunnerResult> resultList : anotherSpecRegistry.resultsMap.values()) {
            for (RunnerResult runnerResult : resultList) {
                addResult(runnerResult);
            }
            LOG.info("[{}] result imtes has been added to list", resultList.size());
        }
    }

    public void addResult(RunnerResult result) {

        Check.notNull(result, "Result can't be empty");

        Collection<RunnerResult> resultsCollection = resultsMap.get(result.getResult());

        Check.isFalse(resultsCollection.contains(result), "Result already stored");

        resultsCollection.add(result);

        LOG.info("New result [{}] has been added", result.getResult());
    }

    public Collection<RunnerResult> getResultsList(Result result) {
        Check.notNull(result, "Result type must be specified");

        Collection<RunnerResult> resultList = Collections.unmodifiableCollection(resultsMap.get(result));

        LOG.debug("Result list [{}] has been created, size [{}]", result, resultList.size());

        return resultList;

    }

    public Integer getResultsAmount(Result result) {

        Check.notNull(result, "Result type must be specified");

        Integer resultsCount = resultsMap.get(result).size();

        return resultsCount;

    }

    @Override
    public String toString() {
        return "SpecificationResultRegistry{" +
                "resultsMap=" + resultsMap +
                '}';
    }
}
