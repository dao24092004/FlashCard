
package com.flashcard.event;


public interface TableActionEvent {
    public void onEdit(int id);
    public void onDelete(int id);
    public void onView(int id);
}

