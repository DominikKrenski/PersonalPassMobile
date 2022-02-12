package org.dominik.pass.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.dominik.pass.R;
import org.dominik.pass.adapters.ItemsAdapter;
import org.dominik.pass.http.dto.DataDTO;
import org.dominik.pass.http.enums.DataType;
import org.dominik.pass.models.wrappers.AddressData;
import org.dominik.pass.models.wrappers.AllData;
import org.dominik.pass.models.wrappers.NoteData;
import org.dominik.pass.models.wrappers.PasswordData;
import org.dominik.pass.models.wrappers.SiteData;
import org.dominik.pass.models.entries.AddressEntry;
import org.dominik.pass.models.entries.NoteEntry;
import org.dominik.pass.models.entries.PasswordEntry;
import org.dominik.pass.models.entries.SiteEntry;
import org.dominik.pass.viewmodels.DataViewModel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ItemsFragment extends Fragment {
  private static final String TAG = "ITEMS_FRAGMENT";
  private DataViewModel dataViewModel;

  private View view;

  private final List<AllData> allData = new LinkedList<>();
  private final List<AddressData> addressData = new LinkedList<>();
  private final List<NoteData> noteData = new LinkedList<>();
  private final List<PasswordData> passwordData = new LinkedList<>();
  private final List<SiteData> siteData = new LinkedList<>();

  public ItemsFragment() {}

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }



  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    view = inflater.inflate(R.layout.fragment_items, container, false);
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    dataViewModel = new ViewModelProvider(requireActivity()).get(DataViewModel.class);

    RecyclerView recyclerView = requireActivity().findViewById(R.id.items_recycler);
    ItemsAdapter itemsAdapter = new ItemsAdapter(dataViewModel);
    LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
    Drawable border = getContext().getDrawable(R.drawable.item_divider);
    DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
    divider.setDrawable(border);

    recyclerView.setAdapter(itemsAdapter);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.addItemDecoration(divider);

    if (savedInstanceState == null)
      dataViewModel.fetchAllData();

    dataViewModel
      .getAllData()
      .observe(getViewLifecycleOwner(), data -> {
        if (data != null) {
          prepareData(data);
          Log.d(TAG, allData.toString());
          itemsAdapter.replaceData(allData);
        }
      });

    dataViewModel
      .getSelectedData()
      .observe(getViewLifecycleOwner(), selectedItem -> {
        switch (selectedItem.getType()) {
          case PASSWORD:
           PasswordData password = passwordData
             .stream()
             .filter(p -> p.getPublicId().equals(selectedItem.getPublicId()))
             .findFirst()
             .orElseThrow(() -> new RuntimeException("Data with given id could not be found"));
            Toast.makeText(getContext(), password.toString(), Toast.LENGTH_LONG).show();
           break;
          case ADDRESS:
            AddressData address = addressData
              .stream()
              .filter(a -> a.getPublicId().equals(selectedItem.getPublicId()))
              .findFirst()
              .orElseThrow(() -> new RuntimeException("Data with given id could not be found"));
            Toast.makeText(getContext(), address.toString(), Toast.LENGTH_LONG).show();
            break;
          case NOTE:
            NoteData note = noteData
              .stream()
              .filter(n -> n.getPublicId().equals(selectedItem.getPublicId()))
              .findFirst()
              .orElseThrow(() -> new RuntimeException("Data with given id could not be found"));
            Toast.makeText(getContext(), note.toString(), Toast.LENGTH_LONG).show();
            break;
          case SITE:
            SiteData site = siteData
              .stream()
              .filter(s -> s.getPublicId().equals(selectedItem.getPublicId()))
              .findFirst()
              .orElseThrow(() -> new RuntimeException("Data with given id could not be found"));
            Toast.makeText(getContext(), site.toString(), Toast.LENGTH_LONG).show();
        }
      });
  }

  private void prepareData(List<DataDTO> dataDto) {
    HashMap<DataType, List<AllData>> dataMap = new HashMap<>();

    allData.clear();
    addressData.clear();
    noteData.clear();
    passwordData.clear();
    siteData.clear();

    dataDto
      .forEach(data -> {
        try {
         switch (data.getType()) {
           case PASSWORD:
             PasswordData password = createPasswordData(data);
             passwordData.add(password);

             if (dataMap.containsKey(DataType.PASSWORD)) {
               dataMap.get(DataType.PASSWORD).add(new AllData(password.getPublicId(), password.getPasswordEntry().getEntryTitle(), password.getType()));
             } else {
               List<AllData> list = new LinkedList<>();
               list.add(new AllData(password.getPublicId(), password.getPasswordEntry().getEntryTitle(), password.getType()));
               dataMap.put(DataType.PASSWORD, list);
             }
             break;
           case ADDRESS:
             AddressData address = createAddressData(data);
             addressData.add(address);

             if (dataMap.containsKey(DataType.ADDRESS)) {
               dataMap.get(DataType.ADDRESS).add(new AllData(address.getPublicId(), address.getAddressEntry().getEntryTitle(), address.getType()));
             } else {
               List<AllData> list = new LinkedList<>();
               list.add(new AllData(address.getPublicId(), address.getAddressEntry().getEntryTitle(), address.getType()));
               dataMap.put(DataType.ADDRESS, list);
             }
             break;
           case SITE:
             SiteData site = createSiteData(data);
             siteData.add(site);

             if (dataMap.containsKey(DataType.SITE)) {
               dataMap.get(DataType.SITE).add(new AllData(site.getPublicId(), site.getSiteEntry().getEntryTitle(), site.getType()));
             } else {
               List<AllData> list = new LinkedList<>();
               list.add(new AllData(site.getPublicId(), site.getSiteEntry().getEntryTitle(), site.getType()));
               dataMap.put(DataType.SITE, list);
             }
             break;
           case NOTE:
             NoteData note = createNoteData(data);
             noteData.add(note);

             if (dataMap.containsKey(DataType.NOTE)) {
               dataMap.get(DataType.NOTE).add(new AllData(note.getPublicId(), note.getNoteEntry().getEntryTitle(), note.getType()));
             } else {
               List<AllData> list = new LinkedList<>();
               list.add(new AllData(note.getPublicId(), note.getNoteEntry().getEntryTitle(), note.getType()));
               dataMap.put(DataType.NOTE, list);
             }
             break;
         }
        } catch (JsonProcessingException ex) {
          ex.printStackTrace();
        }
      });

    // add all items from every collection to be in specific order
    if (dataMap.get(DataType.PASSWORD) != null)
      allData.addAll(dataMap.get(DataType.PASSWORD));

    if (dataMap.get(DataType.ADDRESS) != null)
      allData.addAll(dataMap.get(DataType.ADDRESS));

    if (dataMap.get(DataType.SITE) != null)
      allData.addAll(dataMap.get(DataType.SITE));

    if (dataMap.get(DataType.NOTE) != null)
      allData.addAll(dataMap.get(DataType.NOTE));
  }

  private PasswordData createPasswordData(DataDTO data) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();

    PasswordEntry passwordEntry = mapper.readValue(data.getEntry(), PasswordEntry.class);

    return PasswordData.builder()
      .setPublicId(data.getPublicId())
      .setType(data.getType())
      .setCreatedAt(data.getCreatedAt())
      .setUpdatedAt(data.getUpdatedAt())
      .setPasswordEntry(passwordEntry)
      .build();
  }

  private AddressData createAddressData(DataDTO dataDTO) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();

    AddressEntry addressEntry = mapper.readValue(dataDTO.getEntry(), AddressEntry.class);

    return AddressData.builder()
      .setPublicId(dataDTO.getPublicId())
      .setType(dataDTO.getType())
      .setCreatedAt(dataDTO.getCreatedAt())
      .setUpdatedAt(dataDTO.getUpdatedAt())
      .setAddressEntry(addressEntry)
      .build();
  }

  private SiteData createSiteData(DataDTO dataDTO) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    SiteEntry siteEntry = mapper.readValue(dataDTO.getEntry(), SiteEntry.class);

    return SiteData.builder()
      .setPublicId(dataDTO.getPublicId())
      .setType(dataDTO.getType())
      .setCreatedAt(dataDTO.getCreatedAt())
      .setUpdatedAt(dataDTO.getUpdatedAt())
      .setSiteEntry(siteEntry)
      .build();
  }

  private NoteData createNoteData(DataDTO dataDTO) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    NoteEntry noteEntry = mapper.readValue(dataDTO.getEntry(), NoteEntry.class);

    return NoteData.builder()
      .setPublicId(dataDTO.getPublicId())
      .setType(dataDTO.getType())
      .setCreatedAt(dataDTO.getCreatedAt())
      .setUpdatedAt(dataDTO.getUpdatedAt())
      .setNoteEntry(noteEntry)
      .build();
  }
}