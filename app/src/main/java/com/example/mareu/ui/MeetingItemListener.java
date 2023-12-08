package com.example.mareu.ui;

import com.example.mareu.data.Meeting;

// Architecture MVVM -> l’adapter de la recyclerView ne doit pas appeler le ViewModel ; dans ce cas-là, il sera préférable de passer
// par une interface qui fait le lien entre l’adapter et la MainActivity/Fragment, pour que celle-ci puisse appeler le ViewModel

// Interface permettant d'écouter les clics sur les éléments de la liste de réunions

/**
 * Interface defining methods to listen for clicks on items in the meeting list.
 * This interface serves as a bridge between the RecyclerView adapter and the MainActivity/Fragment,
 * allowing the latter to invoke ViewModel actions.
 */
public interface MeetingItemListener {

    /**
     * Called when a click event occurs on an item in the meeting list, specifically on the delete action.
     *
     * @param meeting The meeting associated with the clicked item.
     */
    void onDeleteClick(Meeting meeting);
}

