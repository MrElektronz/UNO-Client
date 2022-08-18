package de.kbecker.thread.commands;

import com.google.gson.JsonObject;

/**
 * @author Kevin Becker (kevin.becker@stud.th-owl.de)
 */
public abstract class Task {

    public abstract void exec(JsonObject jobj);
}
