package srtm1.rules;

import java.io.File;

import toposwapper.TopoSwapperException;

public interface Action
{
    public void execute() throws TopoSwapperException;
}
