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

package jackpal.androidterm;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import jackpal.androidterm.emulatorview.TermSession;
import jackpal.androidterm.emulatorview.UpdateCallback;
import jackpal.androidterm.util.SessionList;

public class WindowListAdapter extends BaseAdapter implements UpdateCallback {
  private SessionList mSessions;

  public WindowListAdapter(final SessionList sessions) {
    setSessions(sessions);
  }

  public void setSessions(final SessionList sessions) {
    mSessions = sessions;

    if (sessions != null) {
      sessions.addCallback(this);
      sessions.addTitleChangedListener(this);
    } else {
      onUpdate();
    }
  }

  public int getCount() {
    if (mSessions != null) {
      return mSessions.size();
    } else {
      return 0;
    }
  }

  public Object getItem(final int position) { return mSessions.get(position); }

  public long getItemId(final int position) { return position; }

  protected String getSessionTitle(final int position,
                                   final String defaultTitle) {
    TermSession session = mSessions.get(position);
    if (session != null && session instanceof GenericTermSession) {
      return ((GenericTermSession)session).getTitle(defaultTitle);
    } else {
      return defaultTitle;
    }
  }

  public View getView(final int position, final View convertView,
                      final ViewGroup parent) {
    Activity act = findActivityFromContext(parent.getContext());
    View child = act.getLayoutInflater().inflate(R.layout.window_list_item,
                                                 parent, false);
    View close = child.findViewById(R.id.window_list_close);

    TextView label = (TextView)child.findViewById(R.id.window_list_label);
    String defaultTitle = act.getString(R.string.window_title, position + 1);
    label.setText(getSessionTitle(position, defaultTitle));

    final SessionList sessions = mSessions;
    final int closePosition = position;
    close.setOnClickListener(new View.OnClickListener() {
      public void onClick(final View v) {
        TermSession session = sessions.remove(closePosition);
        if (session != null) {
          session.finish();
          notifyDataSetChanged();
        }
      }
    });

    return child;
  }

  public void onUpdate() { notifyDataSetChanged(); }

  private static Activity findActivityFromContext(final Context context) {
    if (context == null) {
      return null;
    } else if (context instanceof Activity) {
      return (Activity)context;
    } else if (context instanceof ContextWrapper) {
      ContextWrapper cw = (ContextWrapper)context;
      return findActivityFromContext(cw.getBaseContext());
    }
    return null;
  }
}
