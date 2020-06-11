/*
 * Copyright (C) 2011 Steven Luo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jackpal.androidterm.util;

import jackpal.androidterm.emulatorview.TermSession;
import jackpal.androidterm.emulatorview.UpdateCallback;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * An ArrayList of TermSessions which allows users to register callbacks in
 * order to be notified when the list is changed.
 */
@SuppressWarnings("serial")
public class SessionList extends ArrayList<TermSession> {
LinkedList<UpdateCallback> callbacks = new LinkedList<UpdateCallback>();
LinkedList<UpdateCallback> titleChangedListeners =
	new LinkedList<UpdateCallback>();
UpdateCallback mTitleChangedListener = new UpdateCallback() {
	public void onUpdate() {
		notifyTitleChanged();
	}
};

public SessionList() {
	super();
}

public SessionList(final int capacity) {
	super(capacity);
}

public void addCallback(final UpdateCallback callback) {
	callbacks.add(callback);
	callback.onUpdate();
}

public boolean removeCallback(final UpdateCallback callback) {
	return callbacks.remove(callback);
}

private void notifyChange() {
	for (UpdateCallback callback : callbacks) {
		callback.onUpdate();
	}
}

public void addTitleChangedListener(final UpdateCallback listener) {
	titleChangedListeners.add(listener);
	listener.onUpdate();
}

public boolean removeTitleChangedListener(final UpdateCallback listener) {
	return titleChangedListeners.remove(listener);
}

private void notifyTitleChanged() {
	for (UpdateCallback listener : titleChangedListeners) {
		listener.onUpdate();
	}
}

@Override
public boolean add(final TermSession object) {
	boolean result = super.add(object);
	object.setTitleChangedListener(mTitleChangedListener);
	notifyChange();
	return result;
}

@Override
public void add(final int index, final TermSession object) {
	super.add(index, object);
	object.setTitleChangedListener(mTitleChangedListener);
	notifyChange();
}

@Override
public boolean addAll(final Collection<? extends TermSession> collection) {
	boolean result = super.addAll(collection);
	for (TermSession session : collection) {
		session.setTitleChangedListener(mTitleChangedListener);
	}
	notifyChange();
	return result;
}

@Override
public boolean addAll(final int index,
                      final Collection<? extends TermSession> collection) {
	boolean result = super.addAll(index, collection);
	for (TermSession session : collection) {
		session.setTitleChangedListener(mTitleChangedListener);
	}
	notifyChange();
	return result;
}

@Override
public void clear() {
	for (TermSession session : this) {
		session.setTitleChangedListener(null);
	}
	super.clear();
	notifyChange();
}

@Override
public TermSession remove(final int index) {
	TermSession object = super.remove(index);
	if (object != null) {
		object.setTitleChangedListener(null);
		notifyChange();
	}
	return object;
}

@Override
public boolean remove(final Object object) {
	boolean result = super.remove(object);
	if (result && object instanceof TermSession) {
		((TermSession)object).setTitleChangedListener(null);
		notifyChange();
	}
	return result;
}

@Override
public TermSession set(final int index, final TermSession object) {
	TermSession old = super.set(index, object);
	object.setTitleChangedListener(mTitleChangedListener);
	if (old != null) {
		old.setTitleChangedListener(null);
	}
	notifyChange();
	return old;
}
}
