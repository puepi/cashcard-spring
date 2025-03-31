package example.cashcard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cashcards")
public class CashCardController {

    private final CashCardRepository cashCardRepository;

    private CashCardController(CashCardRepository cashCardRepository) {
        this.cashCardRepository = cashCardRepository;
    }

    @GetMapping("/{requestedId}")
    private ResponseEntity<CashCard> findById(@PathVariable Long requestedId, Principal principal){
        Optional<CashCard> cashCardOptional=Optional.ofNullable(cashCardRepository.findByIdAndOwner(requestedId,principal.getName()));
        if(cashCardOptional.isPresent()){
            return ResponseEntity.ok(cashCardOptional.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    private ResponseEntity<Void> createCashCard(@RequestBody CashCard cashCard, UriComponentsBuilder ucb){
        CashCard savedCashCard=cashCardRepository.save(cashCard);
        URI locationOfCashCard=ucb.path("/cashcards/{id}")
                .buildAndExpand(savedCashCard.id()).toUri();
        return ResponseEntity.created(locationOfCashCard).build();
    }

//    @GetMapping
//    ResponseEntity<Iterable<CashCard>> findAll(){
//        return ResponseEntity.ok(cashCardRepository.findAll());
//    }

    @GetMapping
    ResponseEntity<List<CashCard>> findAll(Pageable pageable, Principal principal){
        Page<CashCard> page= cashCardRepository.findByOwner(principal.getName(),PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))
                ));
        return ResponseEntity.ok(page.getContent());

    } // <1>
}
