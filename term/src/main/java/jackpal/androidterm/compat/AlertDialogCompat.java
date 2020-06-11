package jackpal.androidterm.compat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogCompat extends AlertDialog {
  // API 11
  public static int THEME_HOLO_TRADITIONAL = 1;
  public static int THEME_HOLO_DARK = 2;
  public static int THEME_HOLO_LIGHT = 3;
  // API 14
  public static int THEME_DEVICE_DEFAULT_DARK = 4;
  public static int THEME_DEVICE_DEFAULT_LIGHT = 5;
  ////////////////////////////////////////////////////////////
  private AlertDialogCompat(final Context context) { super(context); }
  private AlertDialogCompat(
      final Context context, final boolean cancelable,
      final DialogInterface.OnCancelListener cancelListener) {
    super(context, cancelable, cancelListener);
  }
  ////////////////////////////////////////////////////////////
  private static class Api11OrLater extends AlertDialog {
    public Api11OrLater(final Context context, final int theme) {
      super(context, theme);
    }
    public Api11OrLater(final Context context, final boolean cancelable,
                        final DialogInterface.OnCancelListener cancelListener) {
      super(context, cancelable, cancelListener);
    }
  }
  ////////////////////////////////////////////////////////////
  private static class Api14OrLater extends AlertDialog {
    public Api14OrLater(final Context context, final int theme) {
      super(context, theme);
    }
    public Api14OrLater(final Context context, final boolean cancelable,
                        final DialogInterface.OnCancelListener cancelListener) {
      super(context, cancelable, cancelListener);
    }
  }
  ////////////////////////////////////////////////////////////
  public static AlertDialog newInstance(final Context context) {
    return (new AlertDialogCompat(context));
  }
  ////////////////////////////////////////////////////////////
  public static AlertDialog newInstance(final Context context,
                                        final int theme) {
    if (AndroidCompat.SDK >= 14) {
      return (new Api14OrLater(context, theme));
    }
    if (AndroidCompat.SDK >= 11) {
      return (new Api11OrLater(context, theme));
    }
    return (new AlertDialogCompat(context));
  }
  ////////////////////////////////////////////////////////////
  public static AlertDialog
  newInstance(final Context context, final boolean cancelable,
              final DialogInterface.OnCancelListener cancelListener) {
    return (new AlertDialogCompat(context, cancelable, cancelListener));
  }
  ////////////////////////////////////////////////////////////

  public static AlertDialog.Builder newInstanceBuilder(final Context context,
                                                       final int theme) {
    if (AndroidCompat.SDK >= 11) {
      return new Api11OrLaterBuilder(context, theme);
    } else {
      return new AlertDialog.Builder(context);
    }
  }
  private static class Api11OrLaterBuilder extends AlertDialog.Builder {
    public Api11OrLaterBuilder(final Context context) { super(context); }
    public Api11OrLaterBuilder(final Context context, final int theme) {
      super(context, theme);
    }
  }
}
