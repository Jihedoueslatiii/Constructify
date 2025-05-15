package com.example.deliverablerisk.Services;

import com.example.deliverablerisk.Entities.Deliverable;

import java.util.List;

public interface IDeliverableService {
    Deliverable addDeliverable(Deliverable deliverable);

    List<Deliverable> getAllDeliverable();
    Deliverable getDeliverableById(Long idDeliverable);

    Deliverable updateDeliverable(Long idDeliverable, Deliverable deliverable);

    void deleteDeliverable(Long idDeliverable);
}