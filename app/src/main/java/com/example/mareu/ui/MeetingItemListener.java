package com.example.mareu.ui;

import com.example.mareu.data.Meeting;

// Architecture MVVM -> l’adapter de la recyclerView ne doit pas appeler le ViewModel ; dans ce cas-là, il sera préférable de passer
// par une interface qui fait le lien entre l’adapter et la MainActivity/Fragment, pour que celle-ci puisse appeler le ViewModel

// Interface permettant d'écouter les clics sur les éléments de la liste de réunions
public interface MeetingItemListener {

    // Méthode appelée lorsqu'un élément de la liste de réunions est cliqué
    void onDeleteClick(Meeting meeting);
}

