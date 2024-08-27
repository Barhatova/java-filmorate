package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaMapper;
import ru.yandex.practicum.filmorate.storage.mpa.MpaRepository;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import({MpaRepository.class, MpaMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaRepositoryTest {
    private final MpaRepository mpaRepository;

    @Test
    public void shouldGetAllMpa() {
        Collection<Mpa> mpas = mpaRepository.getAllMpa();
        assertThat(mpas.size() == 6);
        Mpa mpa = new Mpa(1, "G");
        assertThat(mpas.stream().findFirst())
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(mpa);
    }

    @Test
    public void shouldGetMpaById() {
        Optional<Mpa> mpas = mpaRepository.getMpaById(1);
        Mpa mpa = new Mpa(1, "G");
        assertThat(mpas)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(mpa);
    }
}
