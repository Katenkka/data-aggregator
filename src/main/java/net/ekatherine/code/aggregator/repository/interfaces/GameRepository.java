package net.ekatherine.code.aggregator.repository.interfaces;

import net.ekatherine.code.aggregator.entity.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long>, FindByExternalIdEntityRepository<Game>
{
}
