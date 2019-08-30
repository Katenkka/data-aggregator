package net.ekatherine.code.aggregator.repository.interfaces;

import net.ekatherine.code.aggregator.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party, Long>
{
}